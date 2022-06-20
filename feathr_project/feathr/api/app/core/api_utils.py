# this file will have all transformations needed, to make purview return value align with unified schema.

def remove_relationship_attributes(purview_entity):
    '''
    Remove the unused relationshipAttributes from purview entities, to make it cleaner
    '''
    del(purview_entity["relationshipAttributes"])