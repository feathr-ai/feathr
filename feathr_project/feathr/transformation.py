import enum
from typing import Type, Union, List, Optional
from abc import ABC, abstractmethod
from jinja2 import Template


class Transformation(ABC):
    """Base class for all transformations that produce feature values."""
    @abstractmethod
    def to_feature_config(self) -> str:
        pass


class RowTransformation(Transformation):
    """Base class for all row-level transformations."""
    pass


class ExpressionTransformation(RowTransformation):
    """A row-level transformations that is defined with the Feathr expression language.
    Attributes:
        expr: expression that transforms the raw value into a new value, e.g. amount * 10.
    """
    def __init__(self, expr: str) -> None:
        super().__init__()
        self.expr = expr

    def to_feature_config(self) -> str:
        tm = Template("""
            "{{expr}}"
        """)
        return tm.render(expr=self.expr)


class WindowAggTransformation(Transformation):
    """Aggregate the value of an expression over a fixed time window. E.g. sum(amount*10) over last 3 days.

    Attributes:
        agg_expr: expression that transforms the raw value into a new value, e.g. amount * 10
        agg_func: aggregation function. e.g. sum, count, max, min, avg.
        window: time span of the aggregation, e.g. 3 days
        group_by: an expression used to group by, same as 'group by' in SQL
        filter: an expression used to filter rows, before aggregation
    """
    def __init__(self, agg_expr: str, agg_func: str, window: str, group_by: Optional[str] = None, filter: Optional[str] = None, limit: Optional[int] = None) -> None:
        super().__init__()
        self.def_expr = agg_expr
        self.agg_func = agg_func
        self.window = window
        self.group_by = group_by
        self.filter = filter
        self.limit = limit

    def to_feature_config(self) -> str:
        tm = Template("""
            "{{windowAgg.def_expr}}"
            window: {{windowAgg.window}}
            agg: {{windowAgg.agg_func}}
            {% if windowAgg.group_by is not none %}
                groupBy: {{windowAgg.group_by}}
            {% endif %}
            {% if windowAgg.filter is not none %}
                filter: {{windowAgg.filter}}
            {% endif %}
            {% if windowAgg.limit is not none %}
                limit: {{windowAgg.limit}}
            {% endif %}
        """)
        return tm.render(windowAgg = self)


class UdfTransform(Transformation):
    """User defined transformation. To be supported.

    Attributes:
        name: name of the user defined function
    """
    def __init__(self, name: str) -> None:
        """

        :param name:
        """
        super().__init__()
        self.name = name
    def to_feature_config(self) -> str:
        pass