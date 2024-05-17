from typing import List


class Attachment:
    def __init__(self, file_name, url):
        self.file_name = file_name
        self.url = url

    def __str__(self):
        return f"{self.file_name}"

    def __repr__(self):
        return self.__str__()


class Message:
    def __init__(self, message: str, message_type, author_name, attachments: List[Attachment]):
        self.message = message
        self.message_type = message_type
        self.author_name = author_name
        self.attachments = attachments

    def __str__(self):
        return f"[{self.message_type}] {self.author_name}: {self.message}"

    def __repr__(self):
        return self.__str__()


class Chat:
    def __init__(self, name, topic, messages: List[Message], channel_type):
        self.name = name
        self.topic = topic
        self.messages = messages
        self.channel_type = channel_type

    def __str__(self):
        return f"Chat {self.name} ({self.channel_type}) - {len(self.messages)} messages"

    def __repr__(self):
        return self.__str__()
