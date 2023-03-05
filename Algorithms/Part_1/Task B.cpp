#include <iostream>
#include <string.h>

using namespace std;

void solve(){
    string text;
    cin >> text;
    int n = text.size();
    int i, j, count = 0;
    char text_arr[n + 1];
    // ���������� ������ � ������ ����.
    strcpy(text_arr, text.c_str());
    // ����� � ����� ������, � ����� �� �������, ��� �������� � ������ ����� �� ����� (�� �������).
    // �� ����, ���� ���� � ����� ������� �������, � � ������ ������� �������� ��� ��������, �� ��� � ���������.
    // ������� ��� ���������� ������� ������� ������ �� 2 ����� � ���������� ������������ ��������.
    // ����� ���� ��� � ���������, �� ���������� � ����� � ������ ������� ������� ������.
    for (i = 0, j = n - 1; i < n / 2 && j >= n / 2; i++, j--){
        if ((int)text_arr[i] == (int)text_arr[j] - 32  || (int)text_arr[i] == (int)text_arr[j] + 32){
            count++;
        }
    }
    // �� ��� ������ ���� ���� ���������� ����������� ����� ���-�� n (n �� �������, � �� ���������)
    // �� �� �������, ��� ��������.
    if (count == n / 2){
        cout << "Possible" << '\n';
        while (count > 0){
            cout << count << " ";
            count--;
        }
    } else {
        cout << "Impossible";
    }
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 
