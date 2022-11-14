from unicodedata import name
from registry.models import AnchorDef, AnchorFeatureDef, DerivedFeatureDef, ExpressionTransformation, FeatureType, ProjectDef, SourceDef, TensorCategory, TypedKey, ValueType, VectorType
from registry.purview_registry import PurviewRegistry

registry = PurviewRegistry("feathrazuretest3-purview1")

proj_id = registry.create_project(ProjectDef("yihui_test_registry","yihui_test_registry",{"obsolete":"False"}))

source_id = registry.create_project_datasource(proj_id,SourceDef(name="source1", qualified_name="yihui_test_registry__source1", path="hdfs://somewhere", type="hdfs"))

anchor1_id = registry.create_project_anchor(proj_id, AnchorDef(
    qualified_name="yihui_test_registry__anchor1", name="anchor1", source_id=source_id))
ft1 = FeatureType(type=VectorType.TENSOR,  tensor_category=TensorCategory.DENSE,
                  dimension_type=[], val_type=ValueType.INT)
t1 = ExpressionTransformation("af1")
k = TypedKey(key_column="c1", key_column_type=ValueType.INT)

feature1 = registry.create_project_anchor_feature(proj_id, anchor1_id, AnchorFeatureDef(
    qualified_name="yihui_test_registry__anchor1__af1", name="af1", feature_type=ft1,  transformation=t1, key=[k]))
derived = registry.create_project_derived_feature(proj_id, DerivedFeatureDef(qualified_name="yihui_test_registry__df1",
                                          name="df1", feature_type=ft1, transformation=t1, key=[k], input_anchor_features=[feature1], input_derived_features=[]))

print(proj_id,source_id,anchor1_id,feature1,derived)

# Delete Anchor Feature
feature1_delid = registry.delete_feature(feature1)
assert str(feature1) == str(feature1_delid)

# Try getting anchor feature but KeyError exception should be thrown
anchor_exists = 1
try:
    af1 = registry.get_entity(feature1)
except KeyError:
    anchor_exists = 0
assert anchor_exists == 0

# Try getting derived feature but KeyError exception should be thrown
derived_exists = 1
try:
    df1 = registry.get_entity(derived)
except KeyError:
    derived_exists = 0
assert derived_exists == 0

# Delete Project
project1_delid = registry.delete_project(proj_id)
assert str(project1_delid) == str(proj_id)

# Try getting project but KeyError exception should be thrown
project_exists = 1
try:
    project1_id = registry.get_entity(proj_id)
except KeyError:
    project_exists = 0
assert project_exists == 0

# cleanup()
