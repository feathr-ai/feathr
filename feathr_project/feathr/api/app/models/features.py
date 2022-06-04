
from typing import List,Dict
from pydantic import BaseModel


class Features(BaseModel):
    """
    Defining contract for input field
    """

    features: List[Dict[str,str]]
