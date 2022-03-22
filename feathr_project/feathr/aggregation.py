from enum import Enum

class Aggregation(Enum):
    NOP = 0
    AVG = 1
    MAX = 2
    MIN = 3
    SUM = 4
    UNION = 5
    ELEMENTWISE_AVG = 6
    ELEMENTWISE_MIN = 7
    ELEMENTWISE_MAX = 8
    ELEMENTWISE_SUM = 9
    LATEST = 10