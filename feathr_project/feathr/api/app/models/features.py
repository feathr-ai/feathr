
from typing import List
from pydantic import BaseModel


class Features(BaseModel):
    """
    Defining contract for input field
    """

    features: list[dict[str,str]]
