from pathlib import Path

import pytest

from feathr import (
    AbsoluteTimeRange, RelativeTimeRange, ObservationSettings,
    HdfsSource,
    TypedKey, ValueType,
    Feature, FeatureAnchor, WindowAggTransformation,
    # Feature types
    FLOAT,
    INPUT_CONTEXT,
    FeatureQuery,
)
from feathr.utils.job_utils import get_result_df


# TODO remove this
def test__mock_data(mock_data_path):
    import pandas as pd
    df = pd.read_csv(mock_data_path)
    print(df)


@pytest.mark.integration
@pytest.mark.parametrize(
    # TODO expected output results
    "observation_time_range,event_timestamp_column,use_latest_feature_data", [
        (
            AbsoluteTimeRange(
                start_time="2021-01-01 05:19:14",
                end_time="2021-01-02 00:00:00",
                time_format="yyyy-MM-dd HH:mm:ss",
            ),
            "lpep_pickup_datetime",
            False,
        ),
        (
            AbsoluteTimeRange(
                start_time="2021-01-01 05:19:14",
                end_time="2021-01-02 00:00:00",
                time_format="yyyy-MM-dd HH:mm:ss",
            ),
            None,
            True,
        ),
        (
            RelativeTimeRange(offset="10h", window="1d"),
            "lpep_pickup_datetime",
            False,
        ),
        (
            RelativeTimeRange(offset="10h", window="1d"),
            None,
            True,
        ),
        (None, "lpep_pickup_datetime", False),
        (None, None, True),
    ],
)
def test__observation_settings(
    feathr_client,
    mock_data_path,
    observation_time_range,
    event_timestamp_column,
    use_latest_feature_data,
):
    # Upload data into dbfs or adls
    if feathr_client.spark_runtime != "local":
        mock_data_path = feathr_client.feathr_spark_launcher.upload_or_get_cloud_path(mock_data_path)

    # Define agg features
    source = HdfsSource(
        name="source",
        path=mock_data_path,
        event_timestamp_column="lpep_pickup_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss",
    )
    location_id_key = TypedKey(
        key_column="DOLocationID",
        key_column_type=ValueType.INT32,
        description="location id key",
        full_name="location_id_key",
    )
    agg_features = [
        Feature(
            name="f_location_max_fare",
            key=location_id_key,
            feature_type=FLOAT,
            transform=WindowAggTransformation(
                agg_expr="fare_amount",
                agg_func="MAX",
                window="1d",
            ),
        ),
    ]
    agg_anchor = FeatureAnchor(
        name="agg_anchor",
        source=source,
        features=agg_features,
    )

    # Define observation features
    features = [
        Feature(
            name="f_trip_distance",
            feature_type=FLOAT,
            transform="trip_distance",
        ),
    ]

    anchor = FeatureAnchor(
        name="anchor",
        source=INPUT_CONTEXT,
        features=features,
    )

    feathr_client.build_features(anchor_list=[agg_anchor, anchor])

    query = [
        FeatureQuery(
            feature_list=["f_location_max_fare", "f_trip_distance"],
            key=location_id_key,
        ),
    ]

    settings = ObservationSettings(
        observation_path=mock_data_path,
        event_timestamp_column=event_timestamp_column,
        timestamp_format="yyyy-MM-dd HH:mm:ss",
        use_latest_feature_data=use_latest_feature_data,
        observation_time_range=observation_time_range,
    )

    output_path = str(Path(Path(mock_data_path).parent, "output.avro"))
    feathr_client.get_offline_features(
        observation_settings=settings,
        feature_query=query,
        output_path=output_path,
    )

    feathr_client.wait_job_to_finish(timeout_sec=5000)

    # download result and assert the returned result
    res_df = get_result_df(feathr_client)
    print(res_df)
