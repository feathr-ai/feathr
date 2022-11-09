__version__ = "0.9.0-rc2"

def get_version():
    return __version__

# Decouple Feathr MAVEN Version from Feathr Python SDK Version
import os
def get_maven_artifact():
    maven_version = os.environ.get("FEATHR_MAVEN_VERSION", __version__)
    return f"com.linkedin.feathr:feathr_2.12:{maven_version}"