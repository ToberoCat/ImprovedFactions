from datapipeline.reader import read_chats
from datapipeline.stringifier import stringify_chat
from datapipeline.writer import write_chat

chats = read_chats()
for chat in chats:
    print(f"Writing chat {chat.name}...")
    content = stringify_chat(chat)
    write_chat(chat, content)
