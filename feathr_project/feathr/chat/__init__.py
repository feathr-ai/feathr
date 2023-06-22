from IPython.core.magic import Magics, line_magic, magics_class

from feathr.chat.feathr_chat import FeathrChat
from feathr.chat.prompt_generator import PromptGenerator
from IPython import get_ipython

chatBot = FeathrChat()
dsl_learned = False


@magics_class
class FeathrMagic(Magics):
    @line_magic
    def feathr(self, question):
        scope = get_ipython().get_local_scope(stack_depth=2)
        client = scope.get("client")
        global dsl_learned
        if client:
            prompt_generator = PromptGenerator(client)
            if not dsl_learned:
                ask_to_teach = "I am going to teach you a DSL, could you learn it?"
                chatBot.ask_llm_in_notebook(ask_to_teach)
                dsl = "\n Feathr DSL: \n" + prompt_generator.get_feathr_dsl_prompts()
                chatBot.ask_llm_in_notebook(dsl)

                ask_to_teach = "Do you want to see the full source code for the APIs?"
                chatBot.ask_llm_in_notebook(ask_to_teach)
                dsl = "\n Feathr DSL: \n" + prompt_generator.get_full_dsl_source_code()
                chatBot.ask_llm_in_notebook(dsl)

                dsl_learned = True
            question_with_prompt = prompt_generator.process_question(question)
            chatBot.ask_llm_in_notebook(question_with_prompt)
        else:
            print(
                "'client' is not defined in the notebook. Please create a FeathrClient instance named as 'client' before using Feathr chat. e.g. client = FeathrClient('/path/to/your/workspace') "
            )


def load_ipython_extension(ipython):
    ipython.register_magics(FeathrMagic)
