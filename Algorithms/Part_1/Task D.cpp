#include <iostream>
using namespace std;

void solve(){
    int a, b, c, d, k, a_last = 0;
    cin >> a >> b >> c >> d >> k;
    
    for (int i = 0; i < k; i++){
        a = a * b - c;
        if (a + c < c){
            a = 0;
            break;
        }
        if (a >= d){
            a = d;
            break;
        } else {
	    if (a == a_last){
	    	break;
	    }	
	}
	a_last = a;
    }
    cout << a;
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
}

// ����� ��������, ���� �� ����� ��������� �� ����� ��� b - c != 0 � a > 1;
// ��������, ���� � ��� ����� b = n, � c = b - 1, �� � ��� ����� ������ a = 1, ������ ������ ��� ������ ��������� ����;

/*
#include <iostream>
using namespace std;

void solve() {
  int a, b, c, d, k, a_last = 0;
  cin >> a >> b >> c >> d >> k;
  if (b - c != 1 || a > 1) {
    for (int i = 0; i < k; i++) {
      a = a * b - c;
      if (a + c < c) {
        a = 0;
        break;
      }
      if (a >= d) {
        a = d;
        break;
      } else {
      	if (a == a_last){
        	break;
        }
      }
      a_last = a;
    }
  }
  cout << a;
}

int main() {
  ios_base::sync_with_stdio(0);
  cin.tie(0);
  cout.tie(0);

  solve();
  return 0;
}
*/