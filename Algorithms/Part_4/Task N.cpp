#include <iostream>
#include <algorithm>
#include <vector>

using namespace std;

void dfs (int i, int i_cur,  vector<vector<int>> &piggy_bank, vector<bool> &used, vector<bool> &broke){
    if (used[i]){
        return;
    }
    used[i] = true;
    for (int u : piggy_bank[i]){
        if (u != i_cur){
            dfs(u, i_cur, piggy_bank, used, broke);
            broke[i] = false;
        }
    }
}

void solve(){
    int n, key, i = 0;
    
    cin >> n;
    
    vector<vector<int>> piggy_bank(n);
    vector<bool> used(n, false);
    vector<bool> broke(n, true);
    
    while (cin >> key){
        piggy_bank[--key].push_back(i);
        i++;
    }

    for (int i = 0; i < n; i++){
        if (!used[i]){
            dfs(i, i, piggy_bank, used, broke);
        }
    }
    
    cout << count(broke.begin(), broke.end(), true);
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 
