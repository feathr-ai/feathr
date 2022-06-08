from datetime import datetime, timedelta
from typing import List, Optional
from feathr.sink import Sink
from feathr.materialization_settings import MaterializationSettings
from feathr.materialization_settings import BackfillTime


# it's completely the same as MaterializationSettings. But we renamed it to improve usability.
# In the future, we may want to rely a separate system other than MaterializationSettings to generate stats.
class MonitoringSettings(MaterializationSettings):
    """Settings about monitoring features.
    """
