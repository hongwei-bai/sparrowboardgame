import socket
import code

server_ip = "127.0.0.1"
server_port = 6044


def build_message(pairs):
    list = []
    for key, value in pairs.items():
        list.append('='.join([key, value]))
    str = ""
    for element in list:
        str = " ".join([str, element])
    return str.strip(" ") + "\r\n"


def parse_message(message):
    pairs = message.decode().split(" ")
    dict = {}
    for pair in pairs:
        a = pair.split("=")
        dict.setdefault(a[0], a[1])
    return dict


def parse_board(board):
    matrix = [[0 for i in range(15)] for i in range(15)]
    count = 0
    for i in range(225):
        if int(board[i]) > 0:
            matrix[int(i/15)][i%15] = board[i]
    return matrix


def parse_move(str):
    if str == None:
        return -1,-1
    list = str.split(",")
    return list


s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
s.connect((server_ip, server_port))
d = {
    "msgid":"connection",
    "player_name":code.player_name
}
s.send(build_message(d).encode())
while True:
    msg = s.recv(4096)
    print(msg)
    if msg != None:
        print(msg)
        d = parse_message(msg)
        move = code.makemove(d.get("sn"), d.get("color"),  parse_board(d.get("board")), parse_move(d.get("prev")))
        outdict = {
            "msgid" : "move",
            "move": str(move[0]) + "," + str(move[1])
        }
        s.send(build_message(outdict).encode())
s.close()







