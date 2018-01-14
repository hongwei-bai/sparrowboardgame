player_name = "simple_py3"


def makemove(sn, color, board, prev):
    for y in range(14, 0, -1):
        for x in range(0, 14):
            if board[x][y] == 0:
                return x,y
    return 7,7

