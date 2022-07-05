import os
from re import sub
from typing import Optional
from uuid import UUID
from fastapi import APIRouter, FastAPI, HTTPException
from starlette.middleware.cors import CORSMiddleware
from registry import *
from registry.purview_registry import PurviewRegistry
from registry.models import AnchorDef, AnchorFeatureDef, DerivedFeatureDef, EntityType, ProjectDef, SourceDef, to_snake

rp = "/v1"
try:
    rp = os.environ["API_BASE"]
    if rp[0] != '/':
        rp = '/' + rp
except:
    pass
print("Using API BASE: ", rp)

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
        return dict([(to_camel(k), to_camel(s[k]) if isinstance(s[k],dict) or isinstance(s[k],list) else s[k]) for k in s])

purview_name = os.environ["PURVIEW_NAME"]
registry = PurviewRegistry(purview_name)
app = FastAPI()
router = APIRouter()

# Enables CORS
app.add_middleware(CORSMiddleware,
                   allow_origins=["*"],
                   allow_credentials=True,
                   allow_methods=["*"],
                   allow_headers=["*"],
                   )


@router.get("/projects",tags=["Project"])
def get_projects() -> list[str]:
    return registry.get_projects()


@router.get("/projects/{project}",tags=["Project"])
def get_projects(project: str) -> dict:
    return to_camel(registry.get_project(project).to_dict())


@router.get("/projects/{project}/datasources",tags=["Project"])
def get_project_datasources(project: str) -> list:
    p = registry.get_entity(project,True)
    source_ids = [s.id for s in p.attributes.sources]
    sources = registry.get_entities(source_ids)
    return list([to_camel(e.to_dict()) for e in sources])


@router.get("/projects/{project}/features",tags=["Project"])
def get_project_features(project: str, keyword: Optional[str] = None) -> list:
    if keyword is None or keyword.strip()=='':
        p = registry.get_entity(project,True)
        feature_ids = [s.id for s in p.attributes.anchor_features] + \
            [s.id for s in p.attributes.derived_features]
        features = registry.get_entities(feature_ids,True)
        return list([to_camel(e.to_dict()) for e in features])
    else:
        efs = registry.search_entity(
            keyword, [EntityType.AnchorFeature, EntityType.DerivedFeature],project=project)
        feature_ids = [ef.id for ef in efs]
        features = registry.get_entities(feature_ids)
        return list([to_camel(e.to_dict()) for e in features])


@router.get("/features/{feature}",tags=["Feature"])
def get_feature(feature: str) -> dict:
    e = registry.get_entity(feature,True)
    if e.entity_type not in [EntityType.DerivedFeature, EntityType.AnchorFeature]:
        raise HTTPException(
            status_code=404, detail=f"Feature {feature} not found")
    return to_camel(e.to_dict())


@router.get("/features/{feature}/lineage",tags=["Feature"])
def get_feature_lineage(feature: str) -> dict:
    lineage = registry.get_lineage(feature)
    return to_camel(lineage.to_dict())


@router.post("/projects",tags=["Project"])
def new_project(definition: dict) -> UUID:
    id = registry.create_project(ProjectDef(**to_snake(definition)))
    return {"guid": str(id)}


@router.post("/projects/{project}/datasources",tags=["Project"])
def new_project_datasource(project: str, definition: dict) -> UUID:
    project_id = registry.get_entity_id(project)
    id = registry.create_project_datasource(project_id, SourceDef(**to_snake(definition)))
    return {"guid": str(id)}


@router.post("/projects/{project}/anchors",tags=["Project"])
def new_project_anchor(project: str, definition: dict) -> UUID:
    project_id = registry.get_entity_id(project)
    id = registry.create_project_anchor(project_id, AnchorDef(**to_snake(definition)))
    return {"guid": str(id)}


@router.post("/projects/{project}/anchors/{anchor}/features",tags=["Project"])
def new_project_anchor_feature(project: str, anchor: str, definition: dict) -> UUID:
    project_id = registry.get_entity_id(project)
    anchor_id = registry.get_entity_id(anchor)
    id = registry.create_project_anchor_feature(project_id, anchor_id, AnchorFeatureDef(**to_snake(definition)))
    return {"guid": str(id)}


@router.post("/projects/{project}/derivedfeatures",tags=["Project"])
def new_project_derived_feature(project: str, definition: dict) -> UUID:
    project_id = registry.get_entity_id(project)
    id = registry.create_project_derived_feature(project_id, DerivedFeatureDef(**to_snake(definition)))
    return {"guid": str(id)}


app.include_router(prefix=rp, router=router)
