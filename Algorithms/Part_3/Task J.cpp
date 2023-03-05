#include <iostream>
#include <string.h>
#include <deque>

using namespace std;

void solve(){
    int n;
    string line;
    cin >> n;
    deque<string> q1, q2;
    while(cin >> line){
        // добавляет в конец очереди
        if (line == "+"){
            cin >> line;
            q2.push_back(line);
            // cout << "+ " << line << endl;
        // добавляет в середину очереди
        } else if (line == "*"){
            cin >> line;
            q2.push_front(line);
        } else if (line == "-"){
            cout << q1.front() << endl;
            q1.pop_front();
        }
        // отвечает за середину очереди (полной), часть которая перед VIP мы храним
        // в q1, а всех остальных в q2 и как раз в начале очереди q2 у нас находится середина 
        if (q1.size() < q2.size()){
            q1.push_back(q2.front());
            q2.pop_front();
        }
    }
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 