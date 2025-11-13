using UnityEngine;

public class EnemyHP : MonoBehaviour
{
    public int HP = 30;  // Vida del enemigo (ajusta el valor inicial según necesites)

    // Método que la Torre llamará para aplicar daño
    public void TakeDamage(int damageAmount)
    {
        HP -= damageAmount;
        Debug.Log($"{gameObject.name} recibió {damageAmount} de daño. Vida restante: {HP}");

        if (HP <= 0)
        {
            Die();
        }
    }

    private void Die()
    {
        Debug.Log($"{gameObject.name} fue eliminado!");
        Destroy(gameObject);  // Destruye el enemigo al morir
    }
}
