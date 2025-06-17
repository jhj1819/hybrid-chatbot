from pydantic import BaseModel
from typing import Optional, Dict, Any

class RefineQueryRequest(BaseModel):
    original_text: str
    initial_dialogflow_response_data: Optional[Dict[str, Any]] = None 