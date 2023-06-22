import os
from typing import Optional
from revChatGPT.V3 import Chatbot
from feathr.chat.notebook_utils import *


class FeathrChat(object):
    def __init__(self):
        key = self.get_api_key()
        self.chat_bot = None
        self.feathr_client = None
        if key:
            self.chat_bot = Chatbot(key)

    def get_api_key(self):
        return os.getenv("CHATGPT_API_KEY", None)

    def ask_llm_in_notebook(self, question_with_prompt: str):
        """Ask LLM model a question within the notebook"""
        if not self.chat_bot:
            key = self.get_api_key()
            if not key:
                raise RuntimeError(
                    "Please set environment variable CHATGPT_API_KEY before using Feathr Chat. You can get your API key for ChatGPT at https://platform.openai.com/account/api-keys. For example, run: os.environ['CHATGPT_API_KEY'] = 'your api key' and retry."
                )
            self.chat_bot = Chatbot(key)

        content = self.chat_bot.ask(question_with_prompt)
        if "```" in content and self.is_a_code_gen_question(question_with_prompt):
            # for debugging
            # res = content
            # print(re.sub(r"```.*```", "", res, flags=re.DOTALL))
            code = extract_code_from_string(content)
            create_new_cell(code)
        else:
            print(content)

    def is_a_code_gen_question(self, question_with_prompt):
        return "explain" not in question_with_prompt.lower() and "what" not in question_with_prompt.lower()
