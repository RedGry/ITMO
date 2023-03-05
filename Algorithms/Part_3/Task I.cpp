#include <iostream>
#include <string.h>
#include <set>
#include <vector>

using namespace std;

void solve(){
    int n, k, p, car;
    int i = 0, count = 0;
    cin >> n >> k >> p;
    
    vector<int> cars;       // Содержит все машинки
    vector<int> last_car;   // Указатели, когда мы брали в последний раз машинку
    vector<int> next_car;   // На каком шаге машинка понадобится, которые уже мы брали
    
    while (cin >> car){
        cars.push_back(car);
        next_car.push_back(100000);
        i++;
    }
    
    for (int i = 0; i < n; i++){
        last_car.push_back(-100000);
    }
    
    // Запоминаем когда мы в последний раз брали машинку (шаг)
    // Заполняем массив, шагами, машинок, с которыми мы уже играли
    for (int i = 0; i < p; i++){
        if (last_car[cars[i] - 1] != -100000){
            next_car[last_car[cars[i] - 1]] = i;
        }
        last_car[cars[i] - 1] = i;
    }

    set<int> cur_cars;  // Храним шаги текущих машинок на полу, когда они 
                        // понадобятся в следующий раз, учитывая вместимость
    
    for (int i = 0; i < p; i++){
        // Проверяем, если у нас машинка, которая у нас на полу
        // (шаг на котором она понадобится с текущим шагом)
        if (!cur_cars.count(i)){
            count++;
            if (cur_cars.size() == k){
                // Удаляем последний добавленный шаг машинки и добавляем следующий
                auto id_car = cur_cars.end();
                cur_cars.erase(--id_car);
            }
        } else {
            cur_cars.erase(i);
        }
        cur_cars.insert(next_car[i]);
    }
    cout << count;
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 

