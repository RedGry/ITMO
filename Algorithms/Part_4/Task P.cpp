#include <iostream>
#include <string.h>
#include <vector>
#include <set>

using namespace std;

void dfs(int vertex, int n, bool direction, vector<bool> &visited, vector<vector<bool>> &g_check){
    visited[vertex] = true;
    for (int i = 0; i < n; i++){
        if (direction){
            if (g_check[i][vertex] && !visited[i]) {
                dfs(i, n, direction, visited, g_check);
            }
        } else {
            if (g_check[vertex][i] && !visited[i]){
                dfs(i, n, direction, visited, g_check);
            }
        }
    }
}

bool check_connectivity(int n, vector<bool> &visited){
    for (int i = 0; i < n; i++){
        if (visited[i]){
            continue;
        } else {
            return false;
        }
    }
    return true;
}

void solve(){
    int n;
    cin >> n;
    vector<vector<int>> graph(n, vector<int> (n));
    vector<vector<bool>> g_check(n, vector<bool>(n));
    vector<bool> visited;
    int i = 0, j = 0, oil;
    
    while (cin >> oil){
        graph[i][j] = oil;
        j++;
        if (j == n){
            i++;
            j = 0;
        }
    }
    
    int l = 0, r = 1000000000;
    
    while (l != r){
        int mid = (l + r) / 2;
        visited = vector<bool>(n, false);
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                g_check[i][j] = graph[i][j] <= mid;
            }
        }
        
        dfs(0, n, 0, visited, g_check);
        
        bool connectivity = false;
        if (check_connectivity(n, visited)){
            visited = vector<bool>(n, false);
            
            dfs(0, n, 1, visited, g_check);
            
            if (!check_connectivity(n, visited)){
                connectivity = true;
            }
        } else connectivity = true;
        
        
        if (connectivity){
            l = mid + 1;
        } else {
            r = mid;
        }
    }
    
    cout << l << endl;
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 