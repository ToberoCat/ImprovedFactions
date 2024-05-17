import os.path

from datapipeline import output_path
from datapipeline.classes import Chat
import re


def write_chat(chat: Chat, content: str):
    fs_safe_chat_name = re.sub(r"[^a-zA-Z0-9]", "_", chat.name)
    file = os.path.join(output_path, fs_safe_chat_name)
    with open(f"{file}.txt", "w") as f:
        f.write(content)
