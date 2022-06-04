import os
from typing import Optional
from fastapi import APIRouter, FastAPI, HTTPException
from starlette.middleware.cors import CORSMiddleware
from registry import *
from registry.db_registry import DbRegistry
from registry.models import EntityType

rp = "/"
try:
    rp = os.environ["API_BASE"]
    if rp[0] != '/':
        rp = '/' + rp
except:
    pass
print("Using API BASE: ", rp)

registry = DbRegistry()
app = FastAPI()
router = APIRouter()

# Enables CORS
app.add_middleware(CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@router.get("/projects")
def get_projects() -> list[str]:
    return registry.get_projects()


@router.get("/projects/{project}")
def get_projects(project: str) -> dict:
    return registry.get_project(project).to_dict()


@router.get("/projects/{project}/datasources")
def get_project_datasources(project: str) -> list:
    p = registry.get_entity(project)
    source_ids = [s.id for s in p.attributes.sources]
    sources = registry.get_entities(source_ids)
    return list([e.to_dict() for e in sources])


@router.get("/projects/{project}/features")
def get_project_features(project: str, keyword: Optional[str] = None) -> list:
    if keyword is None:
        p = registry.get_entity(project)
        feature_ids = [s.id for s in p.attributes.anchor_features] + \
            [s.id for s in p.attributes.derived_features]
        features = registry.get_entities(feature_ids)
        return list([e.to_dict() for e in features])
    else:
        efs = registry.search_entity(keyword, [EntityType.AnchorFeature, EntityType.DerivedFeature])
        feature_ids = [ef.id for ef in efs]
        features = registry.get_entities(feature_ids)
        return list([e.to_dict() for e in features])


@router.get("/features/{feature}")
def get_feature(feature: str) -> dict:
    e = registry.get_entity(feature)
    if e.entity_type not in [EntityType.DerivedFeature, EntityType.AnchorFeature]:
        raise HTTPException(status_code=404, detail=f"Feature {feature} not found")
    return e


@router.get("/features/{feature}/lineage")
def get_feature_lineage(feature: str) -> dict:
    lineage = registry.get_lineage(feature)
    return lineage.to_dict()


app.include_router(prefix = rp, router=router)
