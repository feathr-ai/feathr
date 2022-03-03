import enum
from typing import Type, Union, List, Optional
from abc import ABC, abstractmethod
from jinja2 import Template

class Transformation(ABC):
    @abstractmethod
    def to_feature_config(self) -> str:
        pass

class RowTransformation(Transformation):
    pass

class ExprTransform(RowTransformation):
    def __init__(self, expr: str) -> None:
        super().__init__()
        self.expr = expr

    def to_feature_config(self) -> str:
        tm = Template("""
            def: "{{expr}}"
        """)
        return tm.render(expr = self.expr)

class WindowAggTransform(Transformation):
    def __init__(self, agg_expr: str, agg_func: str, window: str, groupby: Optional[str] = None, filter: Optional[str] = None, limit: Optional[int] = None) -> None:
        super().__init__()
        self.def_expr = agg_expr
        self.agg_func = agg_func
        self.window = window
        self.groupby = groupby
        self.filter = filter
        self.limit = limit

    def to_feature_config(self) -> str:
        tm = Template("""
            def: "{{windowAgg.def_expr}}"
            window: {{windowAgg.window}}
            agg: {{windowAgg.agg_func}}
            {% if windowAgg.groupby is not none %}
                groupBy: {{windowAgg.groupby}}
            {% endif %}
            {% if windowAgg.filter is not none %}
                filter: {{windowAgg.filter}}
            {% endif %}
            {% if windowAgg.limit is not none %}
                limit: {{windowAgg.limit}}
            {% endif %}
        """)
        return tm.render(windowAgg = self)


class UDFTransform(Transformation):
    def __init__(self, name: str) -> None:
        super().__init__()
        self.name = name
    def to_feature_config(self) -> str:
        pass