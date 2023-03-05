#include <iostream>
#include <string.h>
#include <map>

using namespace std;

map<string, int> stack, stack_blocks[100000]; // stack - основной, stack_blocks - хранение пременных до входа в блок, чтобы при выходе вернуть их.

void solve(){
    string line;
    int block = 0;
    while (cin >> line){
        if (line[0] == '{'){
            // с каждым входом в блок увеличиваем перменную, чтобы разделять блоки друг от друга, а при выходе наоборот уменьшаем.
            block++;
        } else if (line[0] == '}'){
            // когда мы выходим из стека, мы в основной стек возвращаем значения, до входа в блок.
            for (auto p: stack_blocks[block]){
                stack[p.first] = p.second;
                //cout << "key = " << p.first << "; val = " << p.second << '\n';
            }
            //cout << "-------------" << '\n';
            stack_blocks[block].clear();
            block--;
        } else {
            string variable1 = "";
            int i = 0;
            // Считывание переменной (var1)
            for (i = 0; i < (int)line.size(); i++){
                if ('a' <= line[i] && line[i] <= 'z'){
                    variable1 += line[i];
                } else {
                    break;
                }
            }
            // Когда мы вводим перменную (var1), мы проверяем была ли она до этого,
            // Если её не было то нужно задать 0 по дефолту.
            if (stack.count(variable1) == 0){
                stack[variable1] = 0;
            }               // Здесь лучше всего наверное сделать проверку на =,
            i++;            // Но по условию наверное не может быть такого, чтобы на этой позиции было что-то другое
            int num;        // наше число в строке
            int sign = 1;   // знак числа в строке
            if (line[i] == '-'){
                sign = -1;
                i++;
            }
            // Считывание переменной (var2) или присвоение числа
            if ('0' <= line[i] && line[i] <= '9'){
                num = 0;
                for (; i < (int)line.size(); i++){
                    num = 10 * num + (line[i] - '0');
                }
                num *= sign;
            } else {
                string variable2 = "";
                for (; i < (int)line.size(); i++){
                    variable2 += line[i];
                }
                num = stack[variable2];
                cout << num << '\n';
            }
            //cout << variable1 + " = " << stack[variable1] << "  ->  " << variable1 << " = " << num << '\n';
            //Если текущей пременной нет в стеке для блоков, то мы его получаем из основного стека, 
            //Чтобы при выходе из блока, вернуть основной стек в порядок.
            if (stack_blocks[block].count(variable1) == 0){
                stack_blocks[block][variable1] = stack[variable1];
            }
            stack[variable1] = num;
        }
    }
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
}