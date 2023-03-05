#include <iostream>
#include <vector>     // vector
#include <algorithm>  // sort

using namespace std;

void solve(){
    int n, k;
    vector <int> price;
    cin >> n >> k;
    int num;
    int sum = 0;
    // Просто заполняем цены.
    for (int i = 0; i < n; i++){
        cin >> num;
        sum += num;
        price.push_back(num);
    }
    // Производим сортировку (quick sort) и переворачиваем, чтобы цены были
    // В порядке убывания т.к. по условию сказано, что персонаж захотел разбить товары
    // По цене, чтобы добиться большей скидки. Логичнее идти от большего к меньшему.
    sort(price.begin(), price.end());
    reverse(price.begin(), price.end());
    // Уменьшаем суммы покупки с учетом скидочного товара
    for (int i = 1; i <= n; i++){
        if (i % k == 0){
            sum -= price[i - 1];
        }
    }
    cout << sum;
    
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
}
