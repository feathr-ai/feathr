from abc import ABC, abstractmethod


class HoconConvertible(ABC):
    """Represent classes that can convert into Frame HOCON config.
    """
    @abstractmethod
    def to_feature_config(self) -> str:
        """Convert the feature anchor definition into internal HOCON format. (For internal use ony)"""
        pass