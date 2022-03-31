OUTPUT_PATH_TAG = "output_path"
# spark config for output format setting
OUTPUT_FORMAT = "spark.feathr.outputFormat"
REDIS_PASSWORD = 'REDIS_PASSWORD'

# 1MB = 1024*1024
MB_BYTES = 1048576

# For use in registry
REGISTRY_VERSION="f"
SOURCE=f'feathr_source_v{REGISTRY_VERSION}'
FEATHR_PROJECT=f'feathr_workspace_v{REGISTRY_VERSION}'
DERIVED_FEATURE=f'feathr_derived_feature_v{REGISTRY_VERSION}'
ANCHOR=f'feathr_anchor_v{REGISTRY_VERSION}'
ANCHOR_FEATURE=f'feathr_anchor_feature_v{REGISTRY_VERSION}'

ARRAY_SOURCE=f"array<feathr_source_v{REGISTRY_VERSION}>"
ARRAY_ANCHOR=f"array<feathr_anchor_v{REGISTRY_VERSION}>"
ARRAY_DERIVED_FEATURE=f"array<feathr_derived_feature_v{REGISTRY_VERSION}>"
ARRAY_ANCHOR_FEATURE=f"array<feathr_anchor_feature_v{REGISTRY_VERSION}>"

PROJECT_TO_DERIVED_FEATURE=f"feathr_project_to_derived_feature_relationship_v{REGISTRY_VERSION}"
PROJECT_TO_ANCHOR=f"feathr_project_to_anchor_relationship_v{REGISTRY_VERSION}"
ANCHOR_TO_SOURCE=f"feathr_anchor_to_source_relationship_v{REGISTRY_VERSION}"
ANCHOR_TO_FEATURE=f"feathr_anchor_to_feature_relationship_v{REGISTRY_VERSION}"
DERIVED_FEATURE_TO_FEATURE=f"feathr_derived_feature_to_feature_relationship_v{REGISTRY_VERSION}"


INPUT_CONTEXT="PASSTHROUGH"