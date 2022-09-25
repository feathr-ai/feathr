from abc import ABC, abstractmethod

from typing import Any, Dict, List, Optional, Tuple


class SecretManagementClient(ABC):
    """This is the abstract class for all the spark launchers. All the Spark launcher should implement those interfaces
    """

    @abstractmethod
    def __init__(self, secret_namespace: str, secret_client) -> None:
        pass

    @abstractmethod
    def get_feathr_secret(self, secret_string: str):
        """
        """
        pass
