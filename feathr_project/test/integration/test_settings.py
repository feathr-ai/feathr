from datetime import datetime, timedelta
from pathlib import Path
from tempfile import TemporaryDirectory

import pandas as pd
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


TIMESTAMP_COL = "timestamp"
KEY_COL = "location_id"
FEATURE_COL = "fare"


@pytest.fixture(scope="session")
def mock_df():
    """Mock data for testing.
    """
    # TODO Currently we're using "today" since `useLatestFeatureData` uses now().
    # Once the behavior is changed to use the latest timestamp in the data, we can use fixed test data instead of creating new one everytime.
    today = datetime.now().date()
    date_range = list(pd.date_range(start=today-timedelta(days=4), end=today, freq="D"))
    return pd.DataFrame({
        TIMESTAMP_COL: date_range + date_range,
        KEY_COL: [1, 1, 1, 1, 1, 2, 2, 2, 2, 2],
        FEATURE_COL: [5.5, 10.0, 6.0, 8.0, 2.5, 38.0, 12.0, 52.0, 180.0, 3.5],
    })


@pytest.mark.integration
def test__observation_settings(feathr_client, mock_df):
    tmp_dir = TemporaryDirectory()

    mock_data_path = str(Path(tmp_dir.name, "mock_data.csv"))
    mock_df.to_csv(str(mock_data_path), index=False)

    # Upload data into dbfs or adls
    if feathr_client.spark_runtime != "local":
        mock_data_path = feathr_client.feathr_spark_launcher.upload_or_get_cloud_path(mock_data_path)

    # Define agg features
    source = HdfsSource(
        name="source",
        path=mock_data_path,
        event_timestamp_column=TIMESTAMP_COL,
        timestamp_format="yyyy-MM-dd",  # yyyy/MM/dd/HH/mm/ss
    )
    key = TypedKey(
        key_column=KEY_COL,
        key_column_type=ValueType.INT32,
        description="key",
        full_name=KEY_COL,
    )
    agg_features = [
        Feature(
            name=f"f_{FEATURE_COL}",
            key=key,
            feature_type=FLOAT,
            transform=WindowAggTransformation(
                agg_expr=f"cast_float({FEATURE_COL})",
                agg_func="MAX",
                window="2d",    # 2 days sliding window
            ),
        ),
    ]
    agg_anchor = FeatureAnchor(
        name="agg_anchor",
        source=source,
        features=agg_features,
    )

    feathr_client.build_features(
        anchor_list=[
            agg_anchor,
        ]
    )

    query = [
        FeatureQuery(
            feature_list=[
                f"f_{FEATURE_COL}",
            ],
            key=key,
        ),
    ]

    test_parameters__expected_values = [
        (
            dict(event_timestamp_column=TIMESTAMP_COL),
            # Max value by the sliding window '2d'
            [5.5, 10., 10., 8., 8., 38., 38., 52., 180., 180.],
        ),
        (
            dict(use_latest_feature_data=True),
            # The latest feature values: Time window is '2d' and thus the feature values for each key is 8.0 and 180.0
            [8.0, 8.0, 8.0, 8.0, 8.0, 180.0, 180.0, 180.0, 180.0, 180.0],
        ),
        (
            dict(
                event_timestamp_column=TIMESTAMP_COL,
                observation_time_range=RelativeTimeRange(offset="3d", window="2d"),
            ),
            # TODO BUG - RelativeTimeRange doesn't have any effect on the result
            [5.5, 10., 10., 8., 8., 38., 38., 52., 180., 180.],
        ),
        (
            dict(
                event_timestamp_column=TIMESTAMP_COL,
                observation_time_range=AbsoluteTimeRange(
                    start_time=mock_df[TIMESTAMP_COL].max().date().isoformat(),
                    end_time=mock_df[TIMESTAMP_COL].max().date().isoformat(),
                    time_format="yyyy-MM-dd",
                ),
            ),
            # TODO BUG - AbsoluteTimeRange doesn't have any effect on the result
            [5.5, 10., 10., 8., 8., 38., 38., 52., 180., 180.],
        ),
    ]

    for test_params, expected_values in test_parameters__expected_values:
        settings = ObservationSettings(
            observation_path=mock_data_path,
            timestamp_format="yyyy-MM-dd",
            **test_params,
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
        res_df.sort_values(by=[KEY_COL, TIMESTAMP_COL], inplace=True)
        assert res_df[f"f_{FEATURE_COL}"].tolist() == expected_values
        # print(res_df)
