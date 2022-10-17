UPDATE usage.time_configuration SET self_link_href = 'https://{domain}' || self_link_href WHERE self_link_href NOT LIKE 'https://{domain}%';
UPDATE usage.time_configuration SET up_link_href = 'https://{domain}' || up_link_href WHERE up_link_href NOT LIKE 'https://{domain}%';
