from feathr.client import FeathrClient
import os

from feathr.chat.source_code_utils import read_source_code_compact


class PromptGenerator(object):
    def __init__(self, client: FeathrClient) -> None:
        self.client = client
        self.module_path = os.path.dirname(os.path.abspath(__file__))
        self.dsl_learned = False

    def get_feathr_dsl_prompts(self):
        prompts = f"""
        Feathr DSL can be used to create features in these steps:
        1) Import required classes, for example:
        from feathr import (
            FeathrClient,
            BOOLEAN, FLOAT, INT32, ValueType,
            Feature, DerivedFeature, FeatureAnchor,
            INPUT_CONTEXT, HdfsSource,
            WindowAggTransformation,
            TypedKey
        )

        2) Define a source processing UDF my_source_udf_name. Put necessary imports within the function body. For example:
        def my_source_udf_name(df: DataFrame) -> DataFrame:
            df.withColumn(feature_field_name, feature_expression)
            
        3) Define a source, for example:
        batch_source = HdfsSource(
            name=source_name,
            path=source_path,
            preprocessing=source_processing_func_name,
        )
        4) Define a key, for example:
        user_id = TypedKey(
            key_column=key_column_in_source_data,
            key_column_type=ValueType.INT32,
            description=key_column_description",
            full_name=project_name_and_key_column_name,
        )
        5) Define a feature, for example:
        feature_object = Feature(
            name=feature_name,
            key=key_name
            feature_type=FLOAT,
            transform=feature_field_name,
        ) 

        feature_list = [ 
            feature_object, 
        ]

        6) Define a feature anchor, for example:
        feature_anchor = FeatureAnchor(
            name=anchor_name, 
            source=batch_source,
            features=feature_list
        )
        7) Use existing client to build and register features. For example,
           Note that you should assume the client object exist and do not need to create a client.
            client.build_features(
                anchor_list=[feature_anchor],
                derived_feature_list=[],
            )
            client.register_features()
        """
        prompts += self.get_materialization_prompts()
        prompts += self.get_test_prompts()
        prompts += self.get_join_dsl_prompts()
        prompts += self.get_features_prompts()
        return prompts

    def get_full_dsl_source_code(self):
        prompts = ""
        client_source_code = "\n Feathr client API: \n" + read_source_code_compact(
            self.module_path, self.module_path + "/../client.py"
        )
        prompts += client_source_code

        definition_source_code = read_source_code_compact(self.module_path, self.module_path + "/../definition")
        prompts += f"""The full classes for the Feathr DSL is here: {definition_source_code}"""
        return prompts

    def get_join_dsl_prompts(self):
        return """
        Feathr DSL syntax can be used to join features with observation data in these steps:
        1) Define a key
        user_id = TypedKey(
            key_column=key_column_in_observation_data,
            key_column_type=ValueType.INT32,
            description=key_column_description",
            full_name=project_name_and_key_column_name,
        )
        2) Define a feature query
        feature_query = FeatureQuery(
            feature_list=[feature_name],
            key=feauture_key,
        )
        3) Define a observation join settings
        settings = ObservationSettings(
            observation_path=user_observation_source_path,
            event_timestamp_column= timestamp_column_name # e.g. "event_timestamp",
            timestamp_format= timestamp_format # e.g. "yyyy-MM-dd"
        )
        4) Call the feature join API with the observation join setting, feature query, and the output path.
        Assume the client is created already. Do not create it here.
        client.get_offline_features(
            observation_settings=settings,
            feature_query=[user_feature_query],
            output_path=user_profile_source_path.rpartition("/")[0] + f"/product_recommendation_features2.avro",
        )
        5) Wait for job to finish to show the result
        client.wait_job_to_finish(timeout_sec=5000)
        
        6) Get the resulting dataframe by calling get_result_df and pass the client as parameter. 
        res_df = get_result_df(client)
        res_df.head()
        

        """

    def get_test_prompts(self):
        return f"""
            The API to test a feature anchor is:
                FeathrTest.run(feature_anchor, mock_df, start_time, end_time).
            It will return a dataframe, while start_time and end_time is optional.
            
            The API to test a derived feature is:
                FeathrTest.run(derived_feature, [[keys]]) which takes a 2-d array as parameter and returns a list of feature value.

        """

    def get_materialization_prompts(self):
        return f"""
            class RedisSink:
                def __init__(self, table_name="user_features")
                
            The API to materialize a feature anchor is:
                feature_names = ["feature_A", "feature_B"]
                redisSink = RedisSink(
                    table_name="user_features"
                )
                settings = MaterializationSettings(
                    name="settings",
                    sinks=[redisSink],
                    feature_names=feature_names,
                )

                client.materialize_features(settings=settings, allow_materialize_non_agg_feature=True)
                client.wait_job_to_finish(timeout_sec=5000)
        """

    def get_features_prompts(self):
        return f"""
            The API to get the previously joined dataset is:
                Get the joined dataframe by calling get_result_df and pass the client as parameter
                res_df = get_result_df(client)
                res_df.head()
        
            The API to get materialized feature values from online storage is:
                fv = client.multi_get_online_feature_values(
                "user_features", [keys], [feature_names]
                print (fv)
            )
        """

    def get_metadata_prompts(self):
        # Get from registry
        # TODO return metadata from registry
        # features = self.client.registry.list_registered_features(self.client.project_name)
        # return f""" registered features are {features}	"""
        return ""

    def process_question(self, question: str):
        prompts = (
            " My question is: "
            + question
            + ". \n Requirement: Please use the provided Feathr DSL if you're able to, do not use API or concept from other feature engineering related solutions. You can assume an instance of FeathrClient is already created and named as 'client'.  If your anwser has code, combine all your code in a block."
        )
        prompts += "\n Context Information:\n"
        prompts += self.get_metadata_prompts()
        if "train" in question.lower():
            prompts = prompts + ". Do not use event timestamp related columns in model training."
        return prompts
