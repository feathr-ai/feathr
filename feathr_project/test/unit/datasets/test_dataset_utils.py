from pathlib import Path
from tempfile import TemporaryDirectory

import pytest

from feathr.datasets.nyc_taxi import NYC_TAXI_SMALL_URL
from feathr.datasets.utils import maybe_download


@pytest.mark.parametrize(
    "expected_bytes", [None, 3924447],
)
def test__maybe_download(expected_bytes: int):
    """Test maybe_download utility function w/ nyc_taxi data cached at Azure blob."""

    tmpdir = TemporaryDirectory()
    dst_path = Path(tmpdir.name, "data.csv")

    # Assert the data is downloaded
    assert maybe_download(
        src_url=NYC_TAXI_SMALL_URL,
        dst_path=dst_path.resolve(),
        expected_bytes=expected_bytes,
    )

    # Assert the data is already exists and thus the function does not download
    assert not maybe_download(
        src_url=NYC_TAXI_SMALL_URL,
        dst_path=dst_path.resolve(),
        expected_bytes=expected_bytes,
    )

    tmpdir.cleanup()


def test__maybe_download__raise_exception():
    """Test maby_download utility function to raise IOError when the expected bytes mismatches."""

    tmpdir = TemporaryDirectory()

    with pytest.raises(IOError):
        maybe_download(
            src_url=NYC_TAXI_SMALL_URL,
            dst_path=Path(tmpdir.name, "data.csv").resolve(),
            expected_bytes=10,
        )

    tmpdir.cleanup()
