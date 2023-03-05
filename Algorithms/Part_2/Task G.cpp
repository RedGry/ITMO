#include <iostream>
#include <string.h>
#include <algorithm> 
#include <vector>
#include <map>

using namespace std;

bool sortbysecond(const pair<char, int> &a, const pair<char, int> &b){
    return a.second > b.second;
}

void solve(){
    vector <pair<char, int>> weight;
    map<char, int> count_char;
    string line;
    int count = 0;
    cin >> line;
    
    // Заполняем вектор пар для каждой буквы: буква-вес и сортим по возрастанию
    for (int i = 0; i < 26; i++){
        int wght;
        cin >> wght;
        weight.push_back(make_pair(char(i + 97), wght));
    }
    sort(weight.begin(), weight.end(), sortbysecond);
    
    // Считаем количество каждого символа и позицию всех остальных символов
    for (int i = 0; i < line.length(); i++){
        count_char[line[i]]++;
        if (count_char[line[i]] == 2) count++;
    }
    //cout << count;
    
    char mas[line.length()];
    int start = 0;
    int end = line.length() - 1;
    for (auto p: weight){
        if (count_char[p.first] > 1){
            mas[start] = p.first;
            mas[end] = p.first;
            start++;
            end--;
            count_char[p.first] -= 2;
        }
        while (count_char[p.first] != 0){
            mas[count] = p.first;
            count_char[p.first]--;
            count++;
        }
        //cout << p.first << endl;
    }
    
    for (auto p: mas){
        cout << p;
    }
    
    
    /*
    cout << line << endl;
    for (auto p: weight){
        cout << p.first << ": " << p.second << endl;
    }
    
    for (auto p: count_char){
        cout << p.first << ": " << p.second << endl;
    }
    */
    
    
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 
