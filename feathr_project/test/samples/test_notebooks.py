from pathlib import Path
from tempfile import TemporaryDirectory

import pytest
try:
    import papermill as pm
    import scrapbook as sb
except ImportError:
    pass  # disable error while collecting tests for non-notebook environments


SAMPLES_DIR = (
    Path(__file__)
    .parent     # .../samples
    .parent     # .../test
    .parent     # .../feathr_project
    .parent     # .../feathr (root of the repo)
    .joinpath("docs", "samples")
)
NOTEBOOK_PATHS = {
    "nyc_taxi_demo": str(SAMPLES_DIR.joinpath("nyc_taxi_demo.ipynb")),
}


@pytest.mark.notebooks
def test__nyc_taxi_demo(tmp_path):
    notebook_name = "nyc_taxi_demo"

    output_tmpdir = TemporaryDirectory()
    output_notebook_path = str(tmp_path.joinpath(f"{notebook_name}.ipynb"))

    pm.execute_notebook(
        input_path=NOTEBOOK_PATHS[notebook_name],
        output_path=output_notebook_path,
        # kernel_name="python3",
        parameters=dict(
            RESOURCE_PREFIX="feathrazuretest3",  # Use the test resource group
            PROJECT_NAME=notebook_name,
            DATA_STORE_PATH=output_tmpdir.name,
            SPARK_CLUSTER="local",
            USE_CLI_AUTH=False,
            SCRAP_RESULTS=True,
        ),
    )

    # Read results from the Scrapbook and assert expected values
    nb = sb.read_notebook(output_notebook_path)
    outputs = nb.scraps

    assert outputs["materialized_feature_values"].data["239"] == pytest.approx([5707., 1480.], abs=1.)
    assert outputs["materialized_feature_values"].data["265"] == pytest.approx([10000., 4160.], abs=1.)
    assert outputs["rmse"].data == pytest.approx(5., abs=2.)
    assert outputs["mae"].data == pytest.approx(2., abs=1.)

    # clean up
    output_tmpdir.cleanup()
