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


@pytest.mark.integration
def test__observation_settings(feathr_client, mock_data_path):

    # TODO
    mock_data_path = str(Path("mock_data.csv").resolve())
    # mock_distance_data_path = str(Path("mock_distance_data.csv").resolve())

    # Upload data into dbfs or adls
    if feathr_client.spark_runtime != "local":
        mock_data_path = feathr_client.feathr_spark_launcher.upload_or_get_cloud_path(mock_data_path)
        # mock_distance_data_path = feathr_client.feathr_spark_launcher.upload_or_get_cloud_path(mock_distance_data_path)

    # Define agg features
    source = HdfsSource(
        name="source",
        path=mock_data_path,
        event_timestamp_column="lpep_pickup_datetime",
        timestamp_format="yyyyMMdd",
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
                agg_expr="cast_float(fare_amount)",
                agg_func="MAX",
                window="20d",
            ),
        ),
    ]
    agg_anchor = FeatureAnchor(
        name="agg_anchor",
        source=source,
        features=agg_features,
    )

    # distance_source = HdfsSource(
    #     name="distance_source",
    #     path=mock_distance_data_path,
    #     event_timestamp_column="lpep_pickup_datetime",
    #     timestamp_format="yyyy-MM-dd HH:mm:ss",
    # )
    # datetime_key = TypedKey(
    #     key_column="lpep_pickup_datetime",
    #     key_column_type=ValueType.INT64,
    #     description="datetime key",
    #     full_name="datetime_key",
    # )
    # features = [
    #     Feature(
    #         name="f_fare_amount",
    #         # name="f_trip_distance",
    #         # key=datetime_key,
    #         feature_type=FLOAT,
    #         transform="fare_amount",
    #         # transform="trip_distance",
    #     ),
    # ]
    # anchor = FeatureAnchor(
    #     name="anchor",
    #     source=INPUT_CONTEXT,
    #     # source=distance_source,
    #     features=features,
    # )

    feathr_client.build_features(
        anchor_list=[
            agg_anchor,
            # anchor,
        ]
    )

    query = [
        FeatureQuery(
            feature_list=[
                "f_location_max_fare",
                # "f_trip_distance",
                # "f_fare_amount",
            ],
            key=location_id_key,
        ),
    ]

    observation_time_range_values = [
        AbsoluteTimeRange(
            start_time="20210102",
            end_time="20210103",
            time_format="yyyyMMdd",
        ),
        # RelativeTimeRange(offset="10h", window="1d"),
        # None,
    ]

    # event_timestamp_column_values = [
    #     "lpep_pickup_datetime",
    #     None,
    # ]

    for obs_time_range in observation_time_range_values:
        settings = ObservationSettings(
            observation_path=mock_data_path,
            event_timestamp_column="lpep_pickup_datetime",# TODOevent_timestamp_column,
            timestamp_format="yyyyMMdd",  # TODO check -- We only support yyyyMMdd format for this. In future, if there is a request, we can
            # use_latest_feature_data=True,  #use_latest_feature_data,
            observation_time_range=obs_time_range,
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
        print(res_df)  #[["lpep_pickup_datetime", "f_location_max_fare", "f_trip_distance"]])
