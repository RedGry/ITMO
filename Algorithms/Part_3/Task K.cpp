#include <iostream>

using namespace std;

struct Segment{
    Segment *prev, *next;
    bool free_m;
    int l, r, p;
    
    Segment(Segment *prev, Segment *next, bool free_m, int l, int r, int p){
        this->prev = prev;
        this->next = next;
        this->free_m = free_m;
        this->l = l;
        this->r = r;
        this->p = p;
        if (prev){
            prev->next = this;
        }
        if (next){
            next->prev = this;
        }
    };
    
    void remove(){
        if (prev){
            prev->next = next;
        }
        if (next){
            next->prev = prev;
        }
    };
};

// Куча из указателей на свободные отрезки и двухсвязный список свободных отрезков
Segment *heap[100001], *r[100001];

bool better(int a, int b){
    return (heap[a]->r - heap[a]->l) > (heap[b]->r - heap[b]->l);
}

void solve(){
    int n, m, k, l;
    int rs[100001]; // номер запроса (статус)
    cin >> n >> m;
    l = 1;
    heap[0] = new Segment(0, 0, true, 0, n, 0);
    
    for (int i = 0; i < m; i++){
        int t;
        cin >> t;
        if (t > 0){
            Segment *prefix = heap[0];
            
            // Запрос отклоняется, если нет свободных ячеек
            if (prefix->r - prefix->l < t){
                rs[k++] = 0;
                cout << "-1" << '\n';
                continue;
            }
            
            rs[k++] = 1;
            r[k - 1] = new Segment(prefix->prev, prefix, false, prefix->l, prefix->l+t, -1);
            cout << 1 + prefix->l << '\n';
            
            prefix->l += t;
            if (prefix->l < prefix->r){
                int a = prefix->p;
                while (true){
                    int q = a;
                    if ((a << 1) + 1 < l && better((a << 1) + 1, q)){
                        q = (a << 1) + 1;
                    }
                    if ((a << 1) + 2 < l && better((a << 1) + 2, q)){
                        q = (a << 1) + 2;
                    }
                    if (a == q){
                        break;
                    }
                    Segment *t = heap[a];
                    heap[a] = heap[q];
                    heap[q] = t;
                    heap[a]->p = a;
                    heap[q]->p = q;
                    a = q;
                }
            } else {
                prefix->remove();
                l--;
                if (l){
                    Segment *t = heap[0];
                    heap[0] = heap[l];
                    heap[l] = t;
                    heap[0]->p = 0;
                    heap[l]->p = l;
                    
                    int a = 0;
                    while (true){
                        int q = a;
                        if ((a << 1) + 1 < l && better((a << 1) + 1, q)){
                            q = (a << 1) + 1;
                        }
                        if ((a << 1) + 2 < l && better((a << 1) + 2, q)){
                            q = (a << 1) + 2;
                        }
                        if (a == q){
                            break;
                        }
                        Segment *t = heap[a];
                        heap[a] = heap[q];
                        heap[q] = t;
                        heap[a]->p = a;
                        heap[q]->p = q;
                        a = q;
                    }
                }
                delete(prefix);
            }
        } else {
            int t_n = -t;
            t_n--;
            
            rs[k++] = 2;
            if (!rs[t_n]){
                continue;
            }
            rs[t_n] = 2;
            
            Segment *prefix = r[t_n], *sp = prefix->prev, *sn = prefix->next;
            bool bp = sp && sp->free_m;
            bool bn = sn && sn->free_m;
            
            // Создаем новый отрезок в кучу, если справа и слева от нашего отрезка
            // не располагается нового отрезка
            if (!bp && !bn){
                prefix->free_m = true;
                
                prefix->p = l;
                heap[l] = prefix;
                int d = l++;
                while (d && better(d, (d - 1) >> 1)){
                    Segment *t = heap[d];
                    heap[d] = heap[(d - 1) >> 1];
                    heap[(d - 1) >> 1] = t;
                    heap[d]->p = d;
                    heap[(d - 1) >> 1]->p = (d - 1) >> 1;
                    d = (d-1) >> 1;
                }
                continue;
            }
            
            // Увеличиваем следующий отрезок, если слева от нашего отрезка
            // не располагается нового отрезка, а справа располагается
            if (!bp){
                sn->l = prefix->l;
                int d = sn->p;
                while (d && better(d, (d - 1) >> 1)){
                    Segment *t = heap[d];
                    heap[d] = heap[(d - 1) >> 1];
                    heap[(d - 1) >> 1] = t;
                    heap[d]->p = d;
                    heap[(d - 1) >> 1]->p = (d - 1) >> 1;
                    d = (d-1) >> 1;
                }
                prefix->remove();
                delete(prefix);
                continue;
            }
            
            // Увеличиваем предыдущий отрезок, если справа от нашего отрезка
            // не располагается нового отрезка, а слева располагается
            if (!bn){
                sp->r = prefix->r;
                int d = sp->p;
                while (d && better(d, (d - 1) >> 1)){
                    Segment *t = heap[d];
                    heap[d] = heap[(d - 1) >> 1];
                    heap[(d - 1) >> 1] = t;
                    heap[d]->p = d;
                    heap[(d - 1) >> 1]->p = (d - 1) >> 1;
                    d = (d-1) >> 1;
                }
                prefix->remove();
                delete(prefix);
                continue;
            }
            
            // Если слева от нашего отрезка располагается свободный отрезок, а
            // справа другой свободный отрезок, то мы удаляем наш отрезок и отрезок справа,
            // а отрезок слева увеличиваем
            sp->r = sn->r;
            int d = sp->p;
            while (d && better(d, (d - 1) >> 1)){
                Segment *t = heap[d];
                heap[d] = heap[(d - 1) >> 1];
                heap[(d - 1) >> 1] = t;
                heap[d]->p = d;
                heap[(d - 1) >> 1]->p = (d - 1) >> 1;
                d = (d-1) >> 1;
            }
            prefix->remove();
            delete(prefix);
            
            int a = sn->p;
            Segment *t = heap[a];
            heap[a] = heap[l-1];
            heap[l-1] = t;
            heap[a]->p = a;
            heap[l-1]->p = l-1;
            l--;
            if (!(a >= l)){
                int d = a;
                while (d && better(d, (d - 1) >> 1)){
                    Segment *t = heap[d];
                    heap[d] = heap[(d - 1) >> 1];
                    heap[(d - 1) >> 1] = t;
                    heap[d]->p = d;
                    heap[(d - 1) >> 1]->p = (d - 1) >> 1;
                    d = (d-1) >> 1;
                }
                while (true){
                    int q = a;
                    if ((a << 1) + 1 < l && better((a << 1) + 1, q)){
                        q = (a << 1) + 1;
                    }
                    if ((a << 1) + 2 < l && better((a << 1) + 2, q)){
                        q = (a << 1) + 2;
                    }
                    if (a == q){
                        break;
                    }
                    Segment *t = heap[a];
                    heap[a] = heap[q];
                    heap[q] = t;
                    heap[a]->p = a;
                    heap[q]->p = q;
                    a = q;
                }
            }
            sn->remove();
            delete(sn);
        }
    }
}

int main(){
    ios_base::sync_with_stdio(0);
    cin.tie(0); cout.tie(0);
    
    solve();
    return 0;
} 

