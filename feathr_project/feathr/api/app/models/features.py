
from typing import List
from pydantic import BaseModel


class Features(BaseModel):
    """
    Defining contract for input field
    """

    features: List[str]