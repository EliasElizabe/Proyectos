using UnityEngine;

public class TowerHP : MonoBehaviour
{
    // Vida de la base
    public int HP = 100;

    // Método para aplicar daño a la base
    public void Damage(int amount)
    {
        HP -= amount;
        Debug.Log("Torre recibió " + amount + " de daño. HP restante: " + HP);

        // Comprobar si la base fue destruida
        if (HP <= 0)
        {
            Die();
        }
    }

    // Lógica cuando la base muere
    private void Die()
    {
        Debug.Log("¡La torre fue destruida!");
        // Acá podrías llamar al Game Over, mostrar pantalla, etc.
    }
}

