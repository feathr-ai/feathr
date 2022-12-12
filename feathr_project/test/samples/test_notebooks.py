from pathlib import Path
from tempfile import TemporaryDirectory
import yaml

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
    "feature_embedding": str(SAMPLES_DIR.joinpath("feature_embedding.ipynb")),
}


@pytest.mark.notebooks
def test__nyc_taxi_demo(config_path, tmp_path):
    notebook_name = "nyc_taxi_demo"

    output_tmpdir = TemporaryDirectory()
    output_notebook_path = str(tmp_path.joinpath(f"{notebook_name}.ipynb"))

    print(f"Running {notebook_name} notebook as {output_notebook_path}")

    pm.execute_notebook(
        input_path=NOTEBOOK_PATHS[notebook_name],
        output_path=output_notebook_path,
        # kernel_name="python3",
        parameters=dict(
            FEATHR_CONFIG_PATH=config_path,
            DATA_STORE_PATH=output_tmpdir.name,
            USE_CLI_AUTH=False,
            REGISTER_FEATURES=False,
            SCRAP_RESULTS=True,
        ),
    )

    # Read results from the Scrapbook and assert expected values
    nb = sb.read_notebook(output_notebook_path)
    outputs = nb.scraps

    assert outputs["materialized_feature_values"].data["239"] == pytest.approx([1480., 5707.], abs=1.)
    assert outputs["materialized_feature_values"].data["265"] == pytest.approx([4160., 10000.], abs=1.)
    assert outputs["rmse"].data == pytest.approx(5., abs=2.)
    assert outputs["mae"].data == pytest.approx(2., abs=1.)


@pytest.mark.databricks
def test__feature_embedding(config_path, tmp_path):
    notebook_name = "feature_embedding"
    output_notebook_path = str(tmp_path.joinpath(f"{notebook_name}.ipynb"))

    print(f"Running {notebook_name} notebook as {output_notebook_path}")

    conf = yaml.safe_load(Path(config_path).read_text())

    pm.execute_notebook(
        input_path=NOTEBOOK_PATHS[notebook_name],
        output_path=output_notebook_path,
        # kernel_name="python3",
        parameters=dict(
            USE_CLI_AUTH=False,
            REGISTER_FEATURES=False,
            SPARK_CONFIG__DATABRICKS__WORKSPACE_INSTANCE_URL=conf["spark_config"]["databricks"]["workspace_instance_url"],
            CLEAN_UP=True,
        ),
    )
