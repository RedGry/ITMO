#include <iostream>

using namespace std;

void solve(){
    int n, k;
    int left, mid, right;
    cin >> n >> k;
    int coords[n];
    
    for (int i = 0; i < n; i++){
        cin >> coords[i];
    }
    // Используя бинарный поиск найдем макс растояния
    // присвоим левую и правую границу нашего поиска
    left = 0;
    right = coords[n - 1] - coords[0] + 1;
    
    // Мы будем делать поиск пока не придем к одному числу на нашем промежутке (решению)
    while (left != right){
        mid = (left + right) / 2;
        // Первую корову мы всегда можем поставить на 1 координату,
        // а остальных уже распределить
        int cows = 1;
        int cow_coord = coords[0];
        // Здесь мы осуществляем поиск количества коров, которое можем расположить
        // При нашем выбранном растоянии mid.
        for (int i = 1; i < n; i++){
            if (coords[i] - cow_coord > mid){
                cow_coord = coords[i];
                cows++;
            }
        }
        // Если мы расположим всех коров на нашем промежутке, то меняем левую границу
        // Если нет, то меняем правую границу. Потому что при слишком маленьком значении
        // mid - мы сможем раставить всех, а при слишком большом - нет
        if (cows >= k){
            left = mid + 1;
        } else {
            right = mid;
        }
    }
    cout << left;
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
}
