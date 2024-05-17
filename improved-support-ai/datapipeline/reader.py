import os
import re
import json

from datapipeline import chat_dumps_path
from datapipeline.classes import Message, Chat, Attachment

def parse_attachments(attachment_dump):
    return [Attachment(attachment["fileName"], attachment["url"]) for attachment in attachment_dump]


def parse_message(message_dump):
    message_type = message_dump["type"]
    content = message_dump["content"]
    user_name = message_dump["author"]["name"]
    attachments = message_dump["attachments"]
    attached_files = []
    if len(attachments) != 0:
        attached_files = parse_attachments(attachments)
    return Message(content, message_type, user_name, attached_files)


def read_dump(dump_location):
    with open(dump_location, "r") as f:
        dump = json.load(f)
    name = dump["channel"]["name"]
    topic = dump["channel"]["topic"]
    messages = dump["messages"]
    channel_type = dump["channel"]["type"]
    messages = [parse_message(message) for message in messages]
    return Chat(name, topic, messages, channel_type)


def read_chats():
    items = os.listdir(chat_dumps_path)
    return [read_dump(os.path.join(chat_dumps_path, item)) for item in items]
