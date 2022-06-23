import registry
from registry.db_registry import quote
from registry.models import AnchorDef, AnchorFeatureDef, DerivedFeatureDef, ExpressionTransformation, FeatureType, ProjectDef, SourceDef, TensorCategory, Transformation, TypedKey, ValueType, VectorType

r = registry.DbRegistry()


def cleanup():
    with r.conn.transaction() as c:
        ids = quote([project1_id, source1_id, anchor1_id, af1_id, df1_id])
        c.execute(
            f"delete from edges where from_id in ({ids}) or to_id in ({ids})")
        c.execute(
            f"delete from entities where entity_id in ({ids})")


project1_id = r.create_project(ProjectDef("unit_test_project_1"))
print("project1 id ", project1_id)
project1 = r.get_entity(project1_id)
assert project1.qualified_name == "unit_test_project_1"

# Re-create project, should return the same id
id = r.create_project(ProjectDef("unit_test_project_1"))
assert project1_id == id

source1_id = r.create_project_datasource(project1_id, SourceDef(
    qualified_name="unit_test_project_1__source1", name="source1", path="hdfs://somewhere", type="hdfs"))
print("source1 id ", source1_id)
anchor1_id = r.create_project_anchor(project1_id, AnchorDef(
    qualified_name="unit_test_project_1__anchor1", name="anchor1", source_id=source1_id))
print("anchor1 id ", anchor1_id)
ft1 = FeatureType(type=VectorType.TENSOR,  tensor_category=TensorCategory.DENSE,
                  dimension_type=[], val_type=ValueType.INT)
t1 = ExpressionTransformation("af1")
k = TypedKey(key_column="c1", key_column_type=ValueType.INT)
af1_id = r.create_project_anchor_feature(project1_id, anchor1_id, AnchorFeatureDef(
    qualified_name="unit_test_project_1__anchor1__af1", name="af1", feature_type=ft1,  transformation=t1, key=[k]))
print("af1 id ", af1_id)

df1_id = r.create_project_derived_feature(project1_id, DerivedFeatureDef(qualified_name="unit_test_project_1__df1",
                                          name="df1", feature_type=ft1, transformation=t1, key=[k], input_anchor_features=[af1_id], input_derived_features=[]))
print("df1 id ", df1_id)

cleanup()
