from abc import ABC, abstractmethod

from typing import Any, Dict, List, Optional, Tuple

class SparkJobLauncher(ABC):

    @abstractmethod
    def upload_to_work_dir(self, local_path_or_http_path: str):
        pass

    @abstractmethod
    def download_result(self, result_path: str, local_folder: str):
        pass


    @abstractmethod
    def submit_feathr_job(self, job_name: str, main_jar_path: str,  main_class_name: str, arguments: List[str],
                          reference_files_path: List[str], job_tags: Dict[str, str] = None,
                          configuration: Dict[str, str] = None):
        pass
    @abstractmethod
    def wait_for_completion(self, timeout_seconds: Optional[float]) -> bool:
        pass

    @abstractmethod
    def get_status(self) -> str:
        pass

