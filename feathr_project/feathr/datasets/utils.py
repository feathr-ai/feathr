"""Dataset utilities
"""
import logging
import math
from pathlib import Path
import requests
from urllib.parse import urlparse

from tqdm import tqdm


log = logging.getLogger(__name__)


def maybe_download(src_url: str, dst_path: str, expected_bytes=None) -> bool:
    """Check if file exists. If not, download and return True. Else, return False.

    Refs:
        https://github.com/microsoft/recommenders/blob/main/recommenders/datasets/download_utils.py

    Args:
        src_url: Source file URL.
        dst_path: Destination path. If the path is a directory, the file name from the source URL will be added.
        expected_bytes (Optional): Expected bytes of the file to verify.

    Returns:
        bool: Whether the file was downloaded or not.
    """
    dst_path = Path(dst_path)

    # If dst_path is a directory and doesn't contain a file name, add the source file name.
    src_filepath = Path(urlparse(src_url).path)
    if dst_path.suffix != src_filepath.suffix:
        dst_path = dst_path.joinpath(src_filepath.name)

    if dst_path.is_file():
        log.info(f"File {str(dst_path)} already exists")
        return False

    # Check dir if exists. If not, create one
    dst_path.parent.mkdir(parents=True, exist_ok=True)

    response = requests.get(src_url, stream=True)
    if response.status_code == 200:
        log.info(f"Downloading {src_url}")
        total_size = int(response.headers.get("content-length", 0))
        block_size = 1024
        num_iterables = math.ceil(total_size / block_size)
        with open(str(dst_path.resolve()), "wb") as file:
            for data in tqdm(
                response.iter_content(block_size),
                total=num_iterables,
                unit="KB",
                unit_scale=True,
            ):
                file.write(data)

        # Verify the file size
        if expected_bytes is not None and expected_bytes != dst_path.stat().st_size:
            # Delete the file since the size is not the same as the expected one.
            dst_path.unlink()
            raise IOError(f"Failed to verify {str(dst_path)}. Maybe interrupted while downloading?")
        else:
            return True

    else:
        print("wtf")
        response.raise_for_status()
        # If not HTTPError yet still cannot download
        raise Exception(f"Problem downloading {src_url}")
