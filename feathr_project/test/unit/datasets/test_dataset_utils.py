from pathlib import Path
from tempfile import TemporaryDirectory
from urllib.parse import urlparse

import pytest

from feathr.datasets.nyc_taxi import NYC_TAXI_SMALL_URL
from feathr.datasets.utils import maybe_download


@pytest.mark.parametrize(
    "dst_filename,expected_bytes",
    [
        ("", 3924447),  # 3924447 is the nyc_taxi sample data's bytes
        ("data.csv", None),
    ],
)
def test__maybe_download(dst_filename: str, expected_bytes: int):
    """Test maybe_download utility function w/ nyc_taxi data cached at Azure blob."""

    tmpdir = TemporaryDirectory()
    dst_path = Path(tmpdir.name, dst_filename)

    # Assert the data is downloaded
    assert maybe_download(
        src_url=NYC_TAXI_SMALL_URL,
        dst_filepath=str(dst_path),
        expected_bytes=expected_bytes,
    )

    # Assert the downloaded file exists. If dst_path is a dir, assert the original filename is used.
    dst_filepath = dst_path if dst_path.suffix else dst_path.joinpath(Path(urlparse(NYC_TAXI_SMALL_URL).path).name)
    assert dst_filepath.is_file()

    # Assert the data is already exists and thus the function does not download
    assert not maybe_download(
        src_url=NYC_TAXI_SMALL_URL,
        dst_filepath=str(dst_path),
        expected_bytes=expected_bytes,
    )

    tmpdir.cleanup()


def test__maybe_download__raise_exception():
    """Test maby_download utility function to raise IOError when the expected bytes mismatches."""

    tmpdir = TemporaryDirectory()

    with pytest.raises(IOError):
        maybe_download(
            src_url=NYC_TAXI_SMALL_URL,
            dst_filepath=Path(tmpdir.name, "data.csv").resolve(),
            expected_bytes=10,
        )

    tmpdir.cleanup()
