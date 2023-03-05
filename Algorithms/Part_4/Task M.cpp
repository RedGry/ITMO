#include <iostream>
#include <string.h>
#include <vector>
#include <set>

using namespace std;

string create_string(int size){
    string line = "";
    while (size != 0){
        line += "X";
        size--;
    }
    return line;
}

void solve(){
    int n, m, x0, y0, x, y;
    cin >> n >> m >> x0 >> y0 >> x >> y;
    
    vector <string> karta;
    string line;
	string answer = "";
    
    // Считываем карту и ставим ограждение
    karta.push_back(create_string(m + 2));
    while (cin >> line){
        karta.push_back('X' + line + 'X');
    }
    karta.push_back(create_string(m + 2));

    vector <vector <int>> timer (n + 2, vector <int>(m + 2));
    vector <vector <char>> way (n + 2, vector <char>(m + 2));
    set <pair <int, pair <int, int>>> position;
    
    // Заполняем массив времени карты
    for (int i = 0; i < n + 2; i ++){
        for (int j = 0; j < m + 2; j++){
            if (karta[i][j] == '#' || karta[i][j] == 'X'){
                timer[i][j] = -1;
                continue;
            }
            timer[i][j] = 1006010;
        }
    }
    
    // Устанавливаем начальные позиции и обнулаяем таймер
    timer[x][y] = 0;
    position.insert({0, {x, y}});
    
    // Идем в обратную сторону от конца к началу (так эффективнее проходить лабиринт)
    while (!position.empty()){
        pair <int, int> pos = position.begin() -> second;
        position.erase(position.begin());
        
        for (int i = -1; i <= 1; i++){
            for (int j = -1; j <= 1; j++){
                // Проверяем, что мы не идём по диагонали и не стоим, а также нет ли на следующем шаге воды или ограждения
                if ((i != 0 && j != 0) || (i == 0 && j == 0) || karta[pos.first + i][pos.second + j] == '#' || karta[pos.first + i][pos.second + j] == 'X'){
                    continue;
                }
                // Подсчитываем сколько в зависимости куда мы идём, сколько вреени мы затрачиваем
                if (timer[pos.first + i][pos.second + j] > timer[pos.first][pos.second] + (karta[pos.first][pos.second] == 'W') + 1){
                    timer[pos.first + i][pos.second + j] = timer[pos.first][pos.second] + (karta[pos.first][pos.second] == 'W') + 1;
                    position.insert({timer[pos.first + i][pos.second + j], {pos.first + i, pos.second + j}});
                    
                    if (i == -1){
                        way[pos.first + i][pos.second + j] = 'S';
                    } else if (j == 1){
                        way[pos.first + i][pos.second + j] = 'W';
                    } else if (i == 1){
                        way[pos.first + i][pos.second + j] = 'N';
                    } else if (j == -1){
                        way[pos.first + i][pos.second + j] = 'E';
                    }
                }
            }
        }
    }
    
    if (timer[x0][y0] == 1006010){
        cout << -1 << endl;
    } else {
        cout << timer[x0][y0] << endl;
        while (!(x0 == x) || !(y0 == y)){
                answer += way[x0][y0];
            if (way[x0][y0] == 'S'){
                x0++;
            } else if (way[x0][y0] == 'W'){
                y0--;
            } else if (way[x0][y0] == 'N'){
                x0--;
            } else if (way[x0][y0] == 'E'){
                y0++;
            }
        }
		cout << answer << endl;
    }
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 