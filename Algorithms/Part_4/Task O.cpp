#include <iostream>
#include <algorithm>
#include <vector>

using namespace std;

bool dfs (int i, vector <vector <int>> &para, int *trade, bool start = true){
    trade[i] = !start;
    bool answer;
    for (auto p : para[i]){
        if (trade[p] == -1){
            answer = dfs(p, para, trade, !start);
        } else if (trade[i] == trade[p]){
            return false;
        }
    }
    return answer;
}

void solve(){
    int n, m;
    cin >> n >> m;
    bool answer;
    vector <vector <int>> para (n);
    int trade[n];
    memset(trade, -1, sizeof(trade));
    
    while (m != 0){
        int p1, p2;
        cin >> p1 >> p2;
        para[--p1].push_back(--p2);
        para[p2].push_back(p1);
        m--;
    }
    
    int i = 0;
    while (n != 0){
        if (trade[i] == -1){
            answer = dfs(i, para, trade);
        }
        i++;
        n--;
    }

    if (answer){
        cout << "YES" << endl;
    } else {
        cout << "NO" << endl;
    }
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 
