import os

chat_dumps_path = os.path.abspath("./dumps")
output_path = os.path.abspath("./output")
attachment_path = os.path.join(output_path, "attachments")

os.makedirs(chat_dumps_path, exist_ok=True)
os.makedirs(output_path, exist_ok=True)
os.makedirs(attachment_path, exist_ok=True)
