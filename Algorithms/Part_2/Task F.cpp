#include <iostream>
#include <string.h>

using namespace std;

/*
    Пояснение:
    Мы просто меняем местами строки, если сумма чисел одной комбинации больше другой 
    a[i]+a[i+1] < a[i+1]+a[i]
    внутренний цикл с каждой внешней итерацией пробегает на 1 меньше, чем в прошлый раз
    ведь мы в первый раз пробежали весь массив и уже переставили последнее число в нужное
    нам место, зачем его ещё раз трогать? Поэтому нам не нужно каждый раз делать так,
    чтобы внутренний цикл пробегал весь массив.
    swap() - меняет местами
*/

void solve(){
    string arr_num[100];
    int i, j, count = 0;
    for (int i = 0; i < 100; i++){
        cin >> arr_num[i];
        if (arr_num[i].size() == 0){
            break;
        }
        count++;
    }
    for(i = count - 1; i > 0; i--){
        for (j = 0; j < i; j++){
            if (arr_num[j] + arr_num[j + 1] < arr_num[j + 1] + arr_num[j]){
                swap(arr_num[j], arr_num[j + 1]); 
            }
        }
    }
    for(int i = 0; i < count; i++){
        cout << arr_num[i];
    }
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 
