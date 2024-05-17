from datapipeline import attachment_path
from datapipeline.classes import Chat, Message, Attachment
import os
import requests

txt_file_endings = ["txt", "log", "lang", "json", "yml"]


def download_attached_file(file_name, url):
    file_name_hashed = hash(f"{file_name}{url}")
    file_name = f"{file_name_hashed}.{file_name.split('.')[-1]}"
    file_location = os.path.join(attachment_path, file_name)
    if os.path.exists(file_location):
        with open(file_location, "r", encoding="utf-8") as f:
            return f.read()

    response = requests.get(url)
    txt = response.content.decode("utf-8")
    with open(file_location, "w", encoding="utf-8") as f:
        f.write(txt)
    return txt


def stringify_attachment(attachment: Attachment):
    if attachment.file_name.split(".")[-1] in txt_file_endings:
        return f"[{attachment.file_name}]\n{download_attached_file(attachment.file_name, attachment.url)}"
    return f"[{attachment.file_name}]\n{attachment.url}"


def stringify_message(message: Message):
    content = f"""{message.author_name}: 
{message.message.strip()}"""
    if len(message.attachments) != 0:
        content += "\nAttachments:\n"
        for attachment in message.attachments:
            content += stringify_attachment(attachment) + "\n"
    return content


def stringify_chat(chat: Chat):
    content = f"""Channel: {chat.name}
Channel Type: {chat.channel_type}
Topic: {chat.topic}

"""

    for message in chat.messages:
        content += stringify_message(message) + "\n"

    return content
