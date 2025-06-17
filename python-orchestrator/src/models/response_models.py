from pydantic import BaseModel

class RefineQueryResponse(BaseModel):
    refined_query: str 