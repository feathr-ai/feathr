import inspect
from re import sub
from typing import List
from urllib.parse import urlparse
from feathr.constants import INPUT_CONTEXT
from feathr.definition.anchor import FeatureAnchor
from feathr.definition.dtype import FeatureType, str_to_value_type, value_type_to_str
from feathr.definition.feature import Feature
from feathr.definition.feature_derivations import DerivedFeature
from feathr.definition.source import HdfsSource, JdbcSource, Source, SnowflakeSource
from pyapacheatlas.core import AtlasProcess,AtlasEntity
from feathr.definition.source import GenericSource, HdfsSource, JdbcSource, SnowflakeSource, Source, SparkSqlSource, KafKaSource, CosmosDbSource, ElasticSearchSource
from feathr.definition.transformation import ExpressionTransformation, Transformation, WindowAggTransformation
from feathr.definition.typed_key import TypedKey
def to_camel(s):
    if not s:
        return s
    if isinstance(s, str):
        if "_" in s:
            s = sub(r"(_)+", " ", s).title().replace(" ", "")
            return ''.join([s[0].lower(), s[1:]])
        return s
    elif isinstance(s, list):
        return [to_camel(i) for i in s]
    elif isinstance(s, dict):
        return dict([(to_camel(k), s[k]) for k in s])


# TODO: need to update the other sources to make the code cleaner           
def source_to_def(source: Source) -> dict:
    ret = {}
    if source.name == INPUT_CONTEXT:
        return {
            "name": INPUT_CONTEXT,
            "type": INPUT_CONTEXT,
            "path": INPUT_CONTEXT,
        }
    elif isinstance(source, HdfsSource):
        ret = {
            "name": source.name,
            "type": urlparse(source.path).scheme,
            "path": source.path,
        }
    elif isinstance(source, KafKaSource):
        ret = {
            "name": source.name,
            "type": "kafka",
            "brokers":source.config.brokers,
            "topics":source.config.topics,
            "schemaStr":source.config.schema.schemaStr
        }
        print("ret is", ret)
    elif isinstance(source, SnowflakeSource):
        ret = {
            "name": source.name,
            "type": "SNOWFLAKE",
            "path": source.path,
        }
    elif isinstance(source, JdbcSource):
        ret = {
            "name": source.name,
            "type": "jdbc",
            "url": source.url,
        }
        if hasattr(source, "dbtable") and source.dbtable:
            ret["dbtable"] = source.dbtable
        if hasattr(source, "query") and source.query:
            ret["query"] = source.query
        if hasattr(source, "auth") and source.auth:
            ret["auth"] = source.auth
    elif isinstance(source, GenericSource):
        ret = source.to_dict()
        ret["name"] = source.name
    elif isinstance(source, SparkSqlSource):
        ret = source.to_dict()
        ret["name"] = source.name
    else:
        raise ValueError(f"Unsupported source type {source.__class__}")
    
    
    if hasattr(source, "preprocessing") and source.preprocessing:
        ret["preprocessing"] = inspect.getsource(source.preprocessing)
    if source.event_timestamp_column:
        ret["eventTimestampColumn"] = source.event_timestamp_column
        ret["event_timestamp_column"] = source.event_timestamp_column
    if source.timestamp_format:
        ret["timestampFormat"] = source.timestamp_format
        ret["timestamp_format"] = source.timestamp_format
    if source.registry_tags:
        ret["tags"] = source.registry_tags
    return ret

    
def anchor_to_def(v: FeatureAnchor) -> dict:
    # Note that after this method, attributes are Camel cased (eventTimestampColumn). 
    # If the old logic works with snake case (event_timestamp_column), make sure you handle them manually. 
    source_id = v.source._registry_id
    ret = {
        "name": v.name,
        "sourceId": str(source_id),
    }
    if v.registry_tags:
        ret["tags"] = v.registry_tags
    return ret


def _correct_function_indentation(user_func: str) -> str:
    """
    The function read from registry might have the wrong indentation. We need to correct those indentations.
    More specifically, we are using the inspect module to copy the function body for UDF for further submission. In that case, there will be situations like this:

    def feathr_udf1(df)
        return df

            def feathr_udf2(df)
                return df

    For example, in Feathr test cases, there are similar patterns for `feathr_udf2`, since it's defined in another function body (the registry_test_setup method).
    This is not an ideal way of dealing with that, but we'll keep it here until we figure out a better way.
    """
    if user_func is None:
        return None
    # if user_func is a string, turn it into a list of strings so that it can be used below
    temp_udf_source_code = user_func.split('\n')
    # assuming the first line is the function name
    leading_space_num = len(
        temp_udf_source_code[0]) - len(temp_udf_source_code[0].lstrip())
    # strip the lines to make sure the function has the correct indentation
    udf_source_code_striped = [line[leading_space_num:]
                               for line in temp_udf_source_code]
    # append '\n' back since it was deleted due to the previous split
    udf_source_code = [line+'\n' for line in udf_source_code_striped]
    return " ".join(udf_source_code)

def transformation_to_def(v: Transformation) -> dict:
    if isinstance(v, ExpressionTransformation):
        return {
            "transformExpr": v.expr
        }
    elif isinstance(v, WindowAggTransformation):
        ret = {
            "defExpr": v.def_expr,
        }
        if v.agg_func:
            ret["aggFunc"] = v.agg_func
        if v.window:
            ret["window"] = v.window
        if v.group_by:
            ret["groupBy"] = v.group_by
        if v.filter:
            ret["filter"] = v.filter
        if v.limit:
            ret["limit"] = v.limit
        return ret
    raise ValueError("Unsupported Transformation type")

def feature_type_to_def(v: FeatureType) -> dict:
    # Note that after this method, attributes are Camel cased (eventTimestampColumn). 
    # If the old logic works with snake case (event_timestamp_column), make sure you handle them manually. 
    return {
        "type": v.type,
        "tensorCategory": v.tensor_category,
        "dimensionType": [value_type_to_str(t) for t in v.dimension_type],
        "valType": value_type_to_str(v.val_type),
    }

def typed_key_to_def(v: TypedKey) -> dict:
    ret = {
        "keyColumn": v.key_column,
        "keyColumnType": value_type_to_str(v.key_column_type)
    }
    if v.full_name:
        ret["fullName"] = v.full_name
    if v.description:
        ret["description"] = v.description
    if v.key_column_alias:
        ret["keyColumnAlias"] = v.key_column_alias
    return ret

def feature_to_def(v: Feature) -> dict:
    ret = {
        "name": v.name,
        "featureType": feature_type_to_def(v.feature_type),
        "key": [typed_key_to_def(k) for k in v.key],
    }
    if v.transform:
        ret["transformation"] = transformation_to_def(
            v.transform)
    if v.registry_tags:
        ret["tags"] = v.registry_tags
    return ret

def derived_feature_to_def(v: DerivedFeature) -> dict:
    # Note that after this method, attributes are Camel cased (eventTimestampColumn). 
    # If the old logic works with snake case (event_timestamp_column), make sure you handle them manually. 
    ret = {
        "name": v.name,
        "featureType": feature_type_to_def(v.feature_type),
        "key": [typed_key_to_def(k) for k in v.key],
        "inputAnchorFeatures": [str(f._registry_id) for f in v.input_features if not isinstance(f, DerivedFeature)],
        "inputDerivedFeatures": [str(f._registry_id) for f in v.input_features if isinstance(f, DerivedFeature)],
    }
    if v.transform:
        ret["transformation"] = transformation_to_def(v.transform)
    return ret

def topological_sort(derived_feature_list: List[DerivedFeature]) -> List[DerivedFeature]:
    """
    In the current registry implementation, we need to make sure all upstream are registered before registering one derived feature
    Thus we need to sort derived features by the partial order of dependencies, upstream to downstream.
    """
    ret = []
    # We don't want to destroy the input list
    input = derived_feature_list.copy()
    
    # Each round add the most downstream features into `ret`, so `ret` is in reversed order
    while input:
        # Process all remaining features
        current = input.copy()
        
        # In Python you should not alter content while iterating
        current_copy = current.copy()
        
        # Go over all remaining features to see if some feature depends on others
        for f in current_copy:
            for i in f.input_features:
                if i in current:
                    # Someone depends on feature `i`, so `i` is **not** the most downstream
                    current.remove(i)
        
        # Now `current` contains only the most downstream features in this round
        ret.extend(current)
        
        # Remove one level of dependency from input
        for f in current:
            input.remove(f)
    
    # The ret was in a reversed order when it's generated
    ret.reverse()
    
    if len(set(ret)) != len (set(derived_feature_list)):
        raise ValueError("Cyclic dependency detected")
    return ret