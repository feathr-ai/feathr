import string
from app.core.registry import getRegistryClient
from app.core.configs import logger

def feature_search(query: string):
    registry_client = getRegistryClient()
    logger.info("Retrieved registry client successfully")
    result = registry_client.search_features(query)
    return result