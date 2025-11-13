using UnityEngine;

public class BombProjectile : MonoBehaviour
{
    private Transform target;
    public float speed = 70f;
    public int damage = 50;
    public float explosionRadius = 3f; // Radio de explosión por defecto
    public GameObject impactEffect;

    public void Seek(Transform _target, int _damage, float _explosionRadius)
    {
        target = _target;
        damage = _damage;
        explosionRadius = _explosionRadius;
    if (target == null)
    {
        Destroy(gameObject);
        return;
    }
    
    // Dispara con fuerza en lugar de movimiento continuo
    Vector3 direction = (target.position - transform.position).normalized;
    rb.AddForce(direction * speed, ForceMode.Impulse);
    
    // Opcional: Añadir rotación de proyectil
    transform.rotation = Quaternion.LookRotation(direction);    
    }

    void Update()
    {
        if (target == null)
        {
            Destroy(gameObject);
            return;
        }

        Vector3 dir = target.position - transform.position;
        float distanceThisFrame = speed * Time.deltaTime;

        if (dir.magnitude <= distanceThisFrame)
        {
            HitTarget();
            return;
        }

        transform.Translate(dir.normalized * distanceThisFrame, Space.World);
        transform.LookAt(target);
    }

    void HitTarget()
    {
        if (impactEffect != null)
        {
            GameObject effectIns = Instantiate(impactEffect, transform.position, transform.rotation);
            Destroy(effectIns, 2f);
        }

        if (explosionRadius > 0f)
        {
            Explode();
        }
        else
        {
            Damage(target);
        }

        Destroy(gameObject);
    }

    void Explode()
    {
        Collider[] colliders = Physics.OverlapSphere(transform.position, explosionRadius);
        foreach (Collider collider in colliders)
        {
            if (collider.CompareTag("Enemy"))
            {
                Damage(collider.transform);
            }
        }
    }

    void Damage(Transform enemy)
    {
        Enemy e = enemy.GetComponent<Enemy>();
        if (e != null)
        {
            e.TakeDamage(damage);
        }
    }

    void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.red;
        Gizmos.DrawWireSphere(transform.position, explosionRadius);
    }
}